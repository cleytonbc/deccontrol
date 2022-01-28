$(document).ready(function() {
	notifica();
	var intervalNotifica = setInterval ( notifica, 60000);
});
 function notifica() {
  // var urlErro = "[[@{/solicitacao/countErro}]]";
   $('#countErro').load(urlErro);
  // var urlLiberada = "[[@{/solicitacao/countLiberada}]]";
	$('#countLiberada').load(urlLiberada);
	};
	