import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TelegramBot {

	private final String endpoint = "https://api.telegram.org/";
	private final String token;
	private boolean isFirstMessage = false;
	private boolean isMainFlow = false;

	public TelegramBot(String token) {
		this.token = token;
	}

	public HttpResponse<JsonNode> sendMessage(Integer chatId, String text) throws UnirestException {
		return Unirest.post(endpoint + "bot" + token + "/sendMessage").field("chat_id", chatId).field("text", text)
				.asJson();
	}

	public HttpResponse<JsonNode> getUpdates(Integer offset) throws UnirestException {
		return Unirest.post(endpoint + "bot" + token + "/getUpdates").field("offset", offset).asJson();
	}

	public void run() throws UnirestException {
		int last_update_id = 0; // controle das mensagens processadas
		HttpResponse<JsonNode> response;

		while (true) {
			response = getUpdates(last_update_id++);
			if (response.getStatus() == 200) {
				JSONArray responses = response.getBody().getObject().getJSONArray("result");
				if (responses.isNull(0)) {
					continue;
				} else {
					last_update_id = responses.getJSONObject(responses.length() - 1).getInt("update_id") + 1;
				}

				for (int i = 0; i < responses.length(); i++) {
					JSONObject message = responses.getJSONObject(i).getJSONObject("message");
					int chat_id = message.getJSONObject("chat").getInt("id");
					String texto = message.getString("text");

					sendFirstMessage(chat_id);
					sendMainFlow(chat_id, isFirstMessage, texto);
				}
			}
		}
	}

	private void sendMainFlow(int chat_id, boolean isFirstMessage, String texto) throws UnirestException {
		if (isFirstMessage) {
			if (texto.contains("oi")) {
				sendMessage(chat_id, "Quer saber quem é você para Manoel? Me diga seu primeiro nome.");
				isMainFlow = true;
			} else if (isMainFlow) {
				sendMessage(chat_id, whorAreYou(texto));
			}
		}
	}

	private void sendFirstMessage(int chat_id) throws UnirestException {
		if (!isFirstMessage) {
			sendMessage(chat_id, "Vamos começar a brincadeira, diga \"oi\".");
			isFirstMessage = true;
		}
	}

	private String whorAreYou(String name) {

		String textoResposta = "";

		switch (name.toLowerCase()) {
		case "catia":
			textoResposta = "This is my wife";
			break;
		case "sheik":
			textoResposta = "This is my dog";
			break;
		case "manoel":
			textoResposta = "This is me";
			break;
		case "manoela":
			textoResposta = "This is my baby";
			break;
		case "carla":
			textoResposta = "This is my sister in law";
			break;
		case "diogo":
			textoResposta = "This is my brother in law";
			break;
		case "cintia":
			textoResposta = "This is my sister in law";
			break;
		case "larissa":
			textoResposta = "This is my niece";
			break;
		case "eduarda":
			textoResposta = "This is my niece";
			break;
		case "lea":
			textoResposta = "This is my mother in law";
			break;
		case "fatima":
			textoResposta = "This is my mother";
			break;
		case "vera":
			textoResposta = "This is fat aunt";
			break;
		case "papai":
			textoResposta = "This is grandpa";
			break;
		default:
			textoResposta = "Pessoa desconhecida.";
		}

		return textoResposta;
	}

}
