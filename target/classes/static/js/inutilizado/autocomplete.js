$(function() {
	var dadosNomeEmpresa
	var dadosCodEmp
	var dadosCodEmpFilial
	var campoNome = document.getElementById('nome');
	var campoCodEmp = document.getElementById('codEmp');
	var campoCodEmpFilial = document.getElementById('codEmpFilial');
	var campoDeclaracao = document.getElementById('declaracao');
	
  $.getJSON('/deccontrol/empresas/listarAtivas').done(function (dados) {
	  // Cria uma array adaptada ao plugin:
	dadosNomeEmpresa = $.map(dados, function (elemento) {
    return {
	      label: elemento.nome,
	      value: elemento.nome,
	      id: elemento.id,
	      codEmp: elemento.codEmp,
	      codEmpFilial: elemento.codEmpFilial,
	      cnpj:elemento.cnpj,
	      nome:elemento.nome
	      
	   };
	  });
	
	dadosCodEmp = $.map(dados, function (elemento) {
	    return {
		      label: elemento.codEmp + " Filial " + elemento.codEmpFilial,
		      value: elemento.codEmp,
		      id: elemento.id,
		      codEmp: elemento.codEmp,
		      codEmpFilial: elemento.codEmpFilial,
		      cnpj:elemento.cnpj,
		      nome:elemento.nome
		      
		   };
		  });
  });

  	campoNome.addEventListener('click', function() {	  
	   // Configura o autocomplete:
	   $( "#nome" ).autocomplete({
		source: dadosNomeEmpresa,
	    select: function (event, ui) {
	      // Usa os dados conforme necessário:
	      $( "#nome" ).val(ui.item.label);
	      preencherDadosPeloNome(ui.item);
	      declaracao.focus();
	    }
	  })
});

campoCodEmp.addEventListener('click', function() {	  
	   // Configura o autocomplete:
	   $( "#codEmp" ).autocomplete({
		source: dadosCodEmp,
	    select: function (event, ui) {
	      // Usa os dados conforme necessário:
	      $( "#codEmp" ).val(ui.item.label);
	      preencherDadosPeloCod(ui.item);
	      declaracao.focus();
	    }
	  })
});

function preencherDadosPeloNome(dados){
	  $("#codEmp").val(dados.codEmp)
	  $("#codEmpFilial").val(dados.codEmpFilial)
	}
function preencherDadosPeloCod(dados){
	  $("#nome").val(dados.nome)
	  $("#codEmpFilial").val(dados.codEmpFilial)
	}

function desabilitarCampos(){
	$("#codEmp").readOnly = true;
}
});