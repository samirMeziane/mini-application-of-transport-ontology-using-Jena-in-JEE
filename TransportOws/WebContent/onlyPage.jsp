<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mini appli OWS</title>
<link rel="stylesheet" href="styles.css">
</head>
<body>
<h1 name="h1"> Bienvenue à vous </h1>




<form action="http://localhost:8080/TransportOws/Transport" method="POST" id="monForm">
<label>transports</label>

<input type="radio" id="Alltransport"  value="AllTransports" onclick="HideFunction()">
<br>
<br>
<div id="transportHiden">
<label> Bus</label>
<input type="radio"   name="trans"  value="Bus">
<label>Metro</label>
<input type="radio"  name="trans"  value="Metro">
<label>Tram</label>
<input type="radio"  name="trans"  value="Tram">
<label>RER</label>
<input type="radio"  name="trans" value="Rer">
</div>

<div id="depnation">
<label>Départ</label><br>
<input  id="Depart" name="Depart"  type="text" onblur="blurDep()">
<label id="error1" style="color: red">vous devez remplir ce champs</label>
<br>
<label>Destination</label><br>
<input  id="Destination" name="Destination"  type="text" onblur="blurDes()">
<label id="error2" style="color: red"> vous devez remplir ce champs</label>
<br>
</div>

<br>
<label>triée par</label>
<select name="trie" onchange="change()" id="select">
<option value="Aucun">pas de trie </option>
<option value="Prix">Prix</option>
<option value="Gaz">émission de gaz</option>
</select> 
<input  type="submit" value="chercher">
<br>
<label id="error3" style="color: red">vous n'avez rempli aucun champs</label>

 <c:forEach var="element" items="${Transports_Type}" > 
  <ul>
    <li>${element}</li>
  </ul>
</c:forEach>



<c:forEach items="${routes}" var="map">
    	<ul>
        <li>Transport :${map.key}</li>
        <li>Prix : ${map.value.getPrix()}</li>
        <li>Gaz : ${map.value.getEmissionDeGaz()}</li>
        
     </ul>
</c:forEach> 
</form>

    
<script type="text/javascript">

document.getElementById("transportHiden").style.display="none";
var transportHIden = document.getElementById("transportHiden");
var Alltransport = document.getElementById("Alltransport");
var depnation = document.getElementById("depnation");
var RecoltedData={};
var TransportTypes=document.getElementsByName("trans");
var Depart;
var Dest;
var error_message_1=document.getElementById("error1");
var error_message_2=document.getElementById("error2");
var error_message_3=document.getElementById("error3");

error_message_1.style.display="none";
error_message_2.style.display="none";
error_message_3.style.display="none";
var trie;
var JsonObj={};



function HideFunction() {
  
  
  if (transportHiden.style.display === "block") {
  	Alltransport.checked=false;  
  	depnation.style.display = "block";
	transportHiden.style.display = "none";
	if("trans" in RecoltedData)
		delete RecoltedData["trans"];
  	}
  else 
  {
	  
	depnation.style.display = "none";
    transportHiden.style.display = "block";
   if("Départ" in RecoltedData)
		delete RecoltedData["Départ"];
    if("Destination" in RecoltedData)
		delete RecoltedData["Destination"];
  }
  
 
 
}

for (var i = 0 ; i < TransportTypes.length; i++) 
{
	TransportTypes[i].onclick= function()
	{
		RecoltedData["trans"]=this.value;
	}
	
}

function blurDep()
{
	dep=document.getElementById("Depart");
	Depart=dep.value;
	if(Depart != "")
	{
		RecoltedData["Départ"]=Depart;
		error_message_1.style.display="none";
	}
	else
	{
		error_message_1.style.display="block";
		if("Départ" in RecoltedData)
			delete RecoltedData["Départ"];	
		
		
	}
}

function blurDes()
{
	desTag=document.getElementById("Destination");
	Des=desTag.value;
	if(Des != "")
	{
		RecoltedData["Destination"]=Des;
		error_message_2.style.display="none";	
	}
		
	else
	{
		if("Destination" in RecoltedData)
			delete RecoltedData["Destination"];
		error_message_2.style.display="block";
		
	}	
	
}

function change()
{
	trie=document.getElementById("select").value
	RecoltedData["trie"]=trie;
}

function submitForm()
{
	
	if(RecoltedData.length == 0 )
		error_message_3.style.display="block";	
	else{
		
		error_message_3.style.display="none";
		for(var key in RecoltedData)
		{
			JsonObj[key]=RecoltedData[key];
			//alert(JsonObj[key])
			
		}
		leng=Object.keys(RecoltedData).length  
		var xhr = new XMLHttpRequest(); 
        var url="";
        var compt=0;
        alert(leng)
        for(key in RecoltedData)
        {
    	   url+=key+"="+RecoltedData[key];	
    	   if(compt<leng-1)
       			url+="&"
    	   	
    	   compt++;
        }
        
        url=("http://localhost:8080/TransportOws/Transport?"+url)
        alert(url);
        xhr.open("GET", url, true); 
        xhr.setRequestHeader("Content-Type", "application/json"); 

        xhr.onreadystatechange = function () { 
           if (xhr.readyState === 4 && xhr.status === 200) {  
                result.innerHTML = this.responseText; 

            } 
        }; 
        alert(typeof JsonObj)
	
        var data = JSON.stringify(JsonObj); 
    	alert(data)
        xhr.send(); 
    	location.replace("http://localhost:8080/TransportOws/Transport")
    	
	}
	//return false;
	
	
	
}

</script>
</body>
</html>
