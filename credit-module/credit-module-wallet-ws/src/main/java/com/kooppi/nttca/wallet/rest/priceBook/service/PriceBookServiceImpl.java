package com.kooppi.nttca.wallet.rest.priceBook.service;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.mq.domain.PriceBookEmsMessage;
import com.kooppi.nttca.portal.common.mq.domain.PricebookItem;
import com.kooppi.nttca.portal.common.mq.service.QueueConsumer;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.wallet.dto.priceBook.GetPriceBookByPartNoDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.GetPriceBookByPartNoItemDto;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookItemDto;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;
import com.kooppi.nttca.wallet.rest.priceBook.repository.PriceBookRepository;

@ApplicationScoped
public class PriceBookServiceImpl implements PriceBookService {
	
	private static final Logger logger = LoggerFactory.getLogger(PriceBookServiceImpl.class);

	@Inject
	private PriceBookRepository priceBookRepository;
	
	@Override
	public ResultList<PriceBook> searchPriceBooksByOrFilter(String globalFilter, String sort, Integer offset, Integer maxRows) {
		String orderBy = null;
		String orderSorting = null;
		if (!sort.isEmpty() && sort != null) {
			orderBy = StringUtils.parseQueryParam(sort).get("orderBy");
			orderSorting = StringUtils.parseQueryParam(sort).get("orderSorting");
		}
		
		return priceBookRepository.searchPriceBooksByOrFilter(globalFilter, orderBy, orderSorting, offset, maxRows);
	}

	@Override
	public Optional<PriceBook> findByUid(Long uid) {
		return priceBookRepository.findByUid(uid);
	}

	@Override
	public Optional<PriceBook> findByPartNumber(String partNumber) {
		return priceBookRepository.findByPartNumber(partNumber);
	}

	@Override
	public List<PriceBookItemDto> findPriceBooksByPartNumber(GetPriceBookByPartNoDto getPriceBookByPartNoDto) {
		
		List<GetPriceBookByPartNoItemDto> partNos = getPriceBookByPartNoDto.getPartNos();
		
//		String[] partNumbersArray = partNumber.split(";");
//		List<String> partNumbers = Arrays.asList(partNumbersArray);
		List<String> partNumbers = partNos.stream().map(partNo -> partNo.getPartNo()).collect(Collectors.toList());
		List<PriceBook> findPriceBooksByPartNumber = priceBookRepository.findPriceBooksByPartNumber(partNumbers);
		List<PriceBookItemDto> priceBookDtos = Lists.newArrayList();
		for (String partNo : partNumbers) {
			if (findPriceBooksByPartNumber.stream().anyMatch(pb -> pb.getPartNo().equals(partNo))) {
				Optional<PriceBook> pbOp = findPriceBooksByPartNumber.stream().filter(pb -> pb.getPartNo().equals(partNo)).findAny();
				PriceBookItemDto pricebookItemDto = pbOp.get().toPricebookItemDto();
				priceBookDtos.add(pricebookItemDto);
			} else {
				PriceBookItemDto dto = new PriceBookItemDto();
				dto.setPartNo(partNo);
				dto.setStatus("NOTFOUND");
				priceBookDtos.add(dto);
			}
		}
		return priceBookDtos;
	}

	@Override
	public Optional<List<PriceBook>> findByBuName(String buName) {
		Optional<List<PriceBook>> pricebooksOp = priceBookRepository.findPriceBooksByBuName(buName);
		return pricebooksOp;
	}
	
	@Override
	public void createPricebook(PriceBook pb) {
		priceBookRepository.create(pb);
	}

	@Override
	public void updatePricebook(PriceBook pb) {
		priceBookRepository.update(pb);
	}
	
	@Override
	public void createOrUpdatePricebookFromEms(String mqMessage) {
		logger.debug("createOrUpdatePricebookFromEms");
		logger.debug("mqMessage: " + mqMessage);
		Gson gson  = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext arg2)
					throws JsonParseException {
				return LocalDate.parse(json.getAsJsonPrimitive().getAsString()).atStartOfDay();
			}
		})
				.create();
		PriceBookEmsMessage object = gson.fromJson(mqMessage, PriceBookEmsMessage.class);

		List<PricebookItem> pricebookItems = object.getContents();
		
		for (PricebookItem pbI : pricebookItems) {
			//check exist or not , no -> create, yes -> update
			Optional<PriceBook> pbOp = findByPartNumber(pbI.getPart_no());
			if (!pbOp.isPresent()) {
				priceBookRepository.create(new PriceBook(pbI));
			} else {
				PriceBook existedPriceBook = pbOp.get();
				if (pbI.getPricebook_effective_date().isAfter(existedPriceBook.getEffectiveDate())) {
					existedPriceBook.edit(pbI);
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		String mes = "\n" + 
				"{\n" + 
				"\"data_date\":\"2018-06-12\",\n" + 
				"\"contents\":[\n" + 
				"{\n" + 
				"\"service_family\":\"GIN\",\n" + 
				"\"service_name\":\"IPBB (IP Transit)\",\n" + 
				"\"short_name\":\"IPBB\",\n" + 
				"\"service_status\":\"Active\",\n" + 
				"\"category_no\":\"CN00247\",\n" + 
				"\"category_name\":\"IP Transit Service Port\",\n" + 
				"\"gl_code\":\"IPBB\",\n" + 
				"\"category_status\":\"Active\",\n" + 
				"\"part_no\":\"S6130\",\n" + 
				"\"product_name\":\"IP Transit Service (Burstable port)\",\n" + 
				"\"product_status\":\"Inactive\",\n" + 
				"\"currency\":\"NTT\",\n" + 
				"\"oneoff_price\":1000.0,\n" + 
				"\"recurring_price\":99999.0,\n" + 
				"\"pricebook_effective_date\":\"2017-01-02\",\n" + 
				"\"pricebook_status\":\"Active\",\n" + 
				"\"providing_company\":\"NTTCA\",\n" + 
				"\"primary_provider\":true\n" + 
				"}\n" + 
				"]\n" + 
				"}";
		Gson gson  = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext arg2)
					throws JsonParseException {
//		        Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsString());
//		        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				return LocalDate.parse(json.getAsJsonPrimitive().getAsString()).atStartOfDay();
			}
		})
				.create();
		PriceBookEmsMessage object = gson.fromJson(mes, PriceBookEmsMessage.class);
		
	}

	@Override
	public List<PriceBook> getAllPriceBooks() {
		List<PriceBook> allPriceBooks = priceBookRepository.getAllPriceBooks();
		return allPriceBooks;
	}

	@Override
	public List<PriceBook> likeSearchPriceBooksByPartNumberAndProductName(String partNo, String productName) {
		return priceBookRepository.likeSearchPriceBooksByPartNumberAndProductName(partNo, productName);
	}
}
