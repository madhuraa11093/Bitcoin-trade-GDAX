package com.takehomeExcercise.api;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.takehomeExcercise.entity.ResponseObject;
import com.takehomeExcercise.entity.ResultObject;

@Path("/transact")
public class CoinBaseBuySaleAPI {

	@GET
	@Path("action/{action}")
	public ResultObject printMessage(@PathParam("action") String action,
			@QueryParam("baseCurrency") String baseCurrency, @QueryParam("quoteCurrency") String quoteCurrency,
			@QueryParam("amount") String amount) throws Exception {

		// Define the API URI where API will be accessed
		ClientRequest request = new ClientRequest(
				"https://api.gdax.com/products/" + baseCurrency + "-" + quoteCurrency + "/book?level=2");
		// Set the accept header to tell the accepted response format
		request.accept("application/json");

		// RESTEasy client automatically converts the response to desired objects.
		// This is how it is done.
		// Populate the response in user object
		ClientResponse<ResponseObject> response = request.get(ResponseObject.class);

		// First validate the api status code
		int apiResponseCode = response.getResponseStatus().getStatusCode();
		if (response.getResponseStatus().getStatusCode() != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + apiResponseCode);
		}

		// Get the api response object from entity
		ResponseObject responseObject = response.getEntity();

		List<List<BigDecimal>> bids = responseObject.getBids();

		// ******************************************************

		if (action.equals("buy")) {
			return buyTranscation(responseObject, baseCurrency, quoteCurrency, amount);
		} else {
			return sellTranscation(responseObject, baseCurrency, quoteCurrency, amount);
		}

		/*
		 * ResultObject rb = new ResultObject(); rb.setCurrency(""); rb.setPrice("");
		 * rb.setTotal("");
		 */

	}

	// **************** asks BTC to USD*************************
	private ResultObject buyTranscation(ResponseObject responseObject, String baseCurrency, String quoteCurrency,
			String amount) {
		// TODO Auto-generated method stub
		List<List<BigDecimal>> asks = responseObject.getAsks();
		ResultObject weightedAverage = new ResultObject();
		if (baseCurrency.equals("BTC")) {
			BigDecimal btc = new BigDecimal(amount);
			BigDecimal tsum = BigDecimal.valueOf(0);
			BigDecimal avg = BigDecimal.valueOf(0);
			BigDecimal tbtc = BigDecimal.valueOf(0);
			BigDecimal diff = BigDecimal.valueOf(0);
			BigDecimal comp = BigDecimal.valueOf(0);
			int j = 0;
			BigDecimal val = BigDecimal.valueOf(0);
			while (j < asks.size()) {
				val = asks.get(j).get(1);
				diff = val.subtract(btc);
				int res = diff.compareTo(comp);
				if (res == 1) {
					tbtc = tbtc.add(btc);
					tsum = tsum.add(btc.multiply(asks.get(j).get(0)));
					avg = tsum.divide(tbtc);
					break;

				} else {

					tbtc = tbtc.add(asks.get(j).get(1));
					tsum = tsum.add(asks.get(j).get(0).multiply(asks.get(j).get(1)));
				}
				btc = diff.abs();
				j++;
			}
			weightedAverage.setCurrency(quoteCurrency);
			weightedAverage.setPrice(avg.divide(new BigDecimal(amount)).toString());
			weightedAverage.setTotal(avg.toString());
			/*
			 * System.out.println("total" +avg); System.out.println("price" + avg.divide(new
			 * BigDecimal(amount))); System.out.println("currency" +quoteCurrency);
			 */

		} else {
			BigDecimal found = BigDecimal.valueOf(0);
			System.out.println(responseObject);
			BigDecimal usd = BigDecimal.valueOf(9999);
			BigDecimal usd1 = BigDecimal.valueOf(9999); //

			// BigDecimal found=0;
			int aditi = 0;
			for (int i = 0; i < asks.size(); i++) {
				found = asks.get(i).get(0);
				int res = found.compareTo(usd);
				if (res == 0) {
					System.out.println(asks.get(i));
					aditi = 1;
					break;
				}
			}
			BigDecimal tot = BigDecimal.valueOf(0);
			BigDecimal sum = BigDecimal.valueOf(0);
			BigDecimal div = BigDecimal.valueOf(0);
			BigDecimal avg = BigDecimal.valueOf(0);
			if (aditi == 0 && found != usd) {
				System.out.println("hellloooooooooooooooooooooooooooooooooooooooo");
				int j = 0;
				usd1 = usd;
				while (j < asks.size()) {
					BigDecimal usdval = BigDecimal.valueOf(0);
					BigDecimal btcval = BigDecimal.valueOf(0);
					usdval = asks.get(j).get(0);
					btcval = asks.get(j).get(1);
					div = usd.divide(asks.get(j).get(0), 7, BigDecimal.ROUND_HALF_UP);
					int comp = (div.compareTo(btcval));
					if (comp == -1) {
						div = usd.divide(usdval, 7, BigDecimal.ROUND_HALF_UP);
						sum = sum.add(usd.multiply(div));
						// tot=tot.add(usd);
						avg = sum.divide(usd1, 7, BigDecimal.ROUND_HALF_UP);
						break;
					} else {
						tot = asks.get(j).get(0).multiply(asks.get(j).get(1));
						usd = usd.subtract(asks.get(j).get(0).multiply(asks.get(j).get(1)));
						sum = sum.add(tot.multiply(asks.get(j).get(1)));

					}
					j++;
				}

				weightedAverage.setCurrency(quoteCurrency);
				weightedAverage.setPrice(avg.divide(new BigDecimal(amount)).toString());
				weightedAverage.setTotal(avg.toString());
			}
		}
		return weightedAverage;

	}

	// **************** BIDS BTC to USD*************************
	private ResultObject sellTranscation(ResponseObject responseObject, String baseCurrency, String quoteCurrency,
			String amount) {
		List<List<BigDecimal>> bids = responseObject.getBids();
		ResultObject weightedAverage = new ResultObject();
		if (baseCurrency.equals("BTC")) {
			BigDecimal btc = new BigDecimal(amount);
			BigDecimal tsum = BigDecimal.valueOf(0);
			BigDecimal avg = BigDecimal.valueOf(0);
			BigDecimal tbtc = BigDecimal.valueOf(0);
			BigDecimal diff = BigDecimal.valueOf(0);
			BigDecimal comp = BigDecimal.valueOf(0);
			int j = 0;
			BigDecimal val = BigDecimal.valueOf(0);
			while (j < bids.size()) {
				val = bids.get(j).get(1);
				diff = val.subtract(btc);
				int res = diff.compareTo(comp);
				if (res == 1) {
					// System.out.println(bids.get(j));

					tbtc = tbtc.add(btc);
					tsum = tsum.add(btc.multiply(bids.get(j).get(0)));
					avg = tsum.divide(tbtc);
					break;

				} else {
					// System.out.println(bids.get(j));
					tbtc = tbtc.add(bids.get(j).get(1));
					tsum = tsum.add(bids.get(j).get(0).multiply(bids.get(j).get(1)));
				}
				btc = diff.abs();
				j++;
			}
			weightedAverage.setCurrency(quoteCurrency);
			weightedAverage.setPrice(avg.divide(new BigDecimal(amount)).toString());
			weightedAverage.setTotal(avg.toString());
		} else {
			BigDecimal found = BigDecimal.valueOf(0);
			BigDecimal usd = new BigDecimal(amount);

			int aditi = 0;
			for (int i = 0; i < bids.size(); i++) {
				found = bids.get(i).get(0);

				int res = found.compareTo(usd);
				if (res == 0) {
					System.out.println(bids.get(i));
					aditi = 1;
					break;
				}
			}
			BigDecimal tot = BigDecimal.valueOf(0);
			BigDecimal sum = BigDecimal.valueOf(0);
			BigDecimal div = BigDecimal.valueOf(0);
			BigDecimal avg = BigDecimal.valueOf(0);
			if (aditi == 0 && found != usd) {

				int j = 0;
				// amount = usd;
				while (j < bids.size()) {
					BigDecimal usdval = BigDecimal.valueOf(0);
					BigDecimal btcval = BigDecimal.valueOf(0);
					usdval = bids.get(j).get(0);
					btcval = bids.get(j).get(1);
					div = usd.divide(bids.get(j).get(0), 7, BigDecimal.ROUND_HALF_UP);

					int comp = (div.compareTo(btcval));
					if (comp == -1) {

						div = usd.divide(usdval, 7, BigDecimal.ROUND_HALF_UP);
						sum = sum.add(usd.multiply(div));

						avg = sum.divide(new BigDecimal(amount), 7, BigDecimal.ROUND_HALF_UP);

						break;
					} else {

						tot = bids.get(j).get(0).multiply(bids.get(j).get(1));

						usd = usd.subtract(bids.get(j).get(0).multiply(bids.get(j).get(1)));

						sum = sum.add(tot.multiply(bids.get(j).get(1)));

					}
					j++;
				}

				weightedAverage.setCurrency(quoteCurrency);
				weightedAverage.setPrice(avg.divide(new BigDecimal(amount)).toString());
				weightedAverage.setTotal(avg.toString());
			}
		}
		return weightedAverage;
	}
	/*
	 * public static void main(String[] args) throws Exception { CoinBaseBuySaleAPI
	 * coin = new CoinBaseBuySaleAPI(); BigDecimal message = coin.printMessage();
	 * System.out.println(message); }
	 */

}
