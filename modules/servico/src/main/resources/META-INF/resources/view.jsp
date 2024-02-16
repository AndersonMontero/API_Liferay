<%@ include file="/init.jsp" %>

<p>
	<b><liferay-ui:message key="servico.caption"/></b>
</p>
<h2>Mensaje:${mensaje}</h2>
<hr/>
<liferay-portlet:actionURL var="postService">
</liferay-portlet:actionURL>
<aui:form action="${postService}">
	<aui:input name="name" type="text" required="true"/>
	<aui:input name="age" type="number" required="true" min="0" max="99"/>
	<aui:input name="phoneNumber" type="tel" required="true" pattern="[0-9]{3}-[0-9]{2}-[0-9]{3}/>
	<aui:input name="address" type="text" required="true"/>
	<aui:button-row>
		<aui:button type="submit"/>
	</aui:button-row>
</aui:form>

<a href="#" onclick="getData()">Consultar Datos</a>
<div id="name"></div>
<div id="age"></div>
<div id="phoneNumber"></div>
<div id="address"></div>
<liferay-portlet:resourceURL var="consultaData">
</liferay-portlet:actionURL>
<aui: script>
	function getData(){
		fetch('${consultaData}')
		.then(resp => resp.json())
		.then(data =>{document.getElementById("name").textContent=data.name;
		document.getElementById("age").textContent=data.age;
		document.getElementById("phoneNumber").textContent=data.phoneNumber;
		document.getElementById("address").textContent=data.address;
		})
	}
