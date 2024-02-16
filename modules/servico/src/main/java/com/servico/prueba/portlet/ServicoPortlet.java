package com.servico.prueba.portlet;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.servico.prueba.constants.JsonPersona;
import com.servico.prueba.constants.Persona;
import com.servico.prueba.constants.ServicoPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import org.osgi.service.component.annotations.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.liferay.portal.kernel.util.ContentTypes.APPLICATION_JSON;

/**
 * @author PC-Anderson
 */
@Component(
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Servico",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + ServicoPortletKeys.SERVICO,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class ServicoPortlet extends MVCPortlet {

	@Override
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException {
		var	name = ParamUtil.getString(actionRequest, "name");
		int age = ParamUtil.getInteger(actionRequest, "age");
		var phoneNumber = ParamUtil.getString(actionRequest, "phoneNumber");
		var address = ParamUtil.getString(actionRequest,"address");
		try {
			// Especifica la URL del servicio que deseas consumir
			String serviceUrl = "https://8e7c6b8a-fc46-4674-a529-4ebec57295d3.mock.pstmn.io/customers";

			// Crea una instancia de URL y HttpsURLConnection
			URL url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Establece el método de solicitud (GET, POST, etc.)
			connection.setRequestMethod("POST");

			connection.setDoOutput(true);

			// Crea un objeto Java que deseas enviar como JSON
			Persona requestBody = new Persona();
			requestBody.setName(name);
			requestBody.setAge(age);
            requestBody.setPhoneNumber(phoneNumber);
            requestBody.setAddress(address);
			// Convierte el objeto Java a JSON
            JsonPersona objectMapper;
            objectMapper = new JsonPersona();
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            System.out.println("Respuesta del jsonBody: " + jsonBody);

			// Establece las propiedades de la solicitud (tipo de contenido y longitud del cuerpo)
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", String.valueOf(jsonBody.length()));

			// Obtiene el flujo de salida para escribir el cuerpo de la solicitud
			try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
				out.write(jsonBody.getBytes(StandardCharsets.UTF_8));
			}

			// Obtiene la respuesta del servicio
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Lee la respuesta del servicio
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// Procesa la respuesta del servicio como sea necesario
				System.out.println("Respuesta del servicio: " + response.toString());

			} else {
				System.out.println("Error al consumir el servicio. Código de respuesta: " + responseCode);
			}

			// Cierra la conexión
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		super.processAction(actionRequest, actionResponse);
	}

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		renderRequest.setAttribute("mensaje","pruebas");
		super.render(renderRequest, renderResponse);
	}

	@Override
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException, PortletException {

		var jsonResponse = JSONFactoryUtil.createJSONObject();
		resourceResponse.setContentType(APPLICATION_JSON);
		try {
			// Especifica la URL del servicio que deseas consumir
			String serviceUrl = "https://8e7c6b8a-fc46-4674-a529-4ebec57295d3.mock.pstmn.io/customers";

			// Crea una instancia de URL y HttpsURLConnection
			URL url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Establece el método de solicitud (GET, POST, etc.)
			connection.setRequestMethod("GET");

			// Obtiene la respuesta del servicio
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Lee la respuesta del servicio
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// Procesa la respuesta del servicio como sea necesario
				System.out.println("Respuesta del servicio: " + response.toString());
				resourceResponse.getWriter().print(response.toString());
			} else {
				System.out.println("Error al consumir el servicio. Código de respuesta: " + responseCode);
			}

			// Cierra la conexión
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		super.serveResource(resourceRequest, resourceResponse);
	}
}