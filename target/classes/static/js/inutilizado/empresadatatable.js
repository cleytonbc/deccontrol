$(document).ready( function () {
	 var table = $('#empresaTable').DataTable({
		 "processing": true,
	   //   "serverSide": true,
	//	 "pagingType": "full_numbers",
		 	"language": {
		 		"search": "Buscar:",
		 		"emptyTable":     "Não existe dados na tabela",
		 		"loadingRecords": "Carregando...",
		 		"processing":"Processando...",
		 		"lengthMenu":     "Exibir _MENU_ entradas",
		 		"info":           "Exibindo _START_ a _END_ de _TOTAL_ entradas",
		 		 "paginate": {
		 		        "first":      "Primeiro",
		 		        "last":       "Último",
		 		        "next":       "Próximo",
		 		        "previous":   "Anterior"
		 		  },
	        },
			"sAjaxSource": "/deccontrol/empresas/listarTodas",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "codEmp"},
		      { "mData": "codEmpFilial" ,                     
                  "render": function ( url ) {
                      return '<a href= '+ url  + ' target=_blank> Clique aqui </a>';
                  }},
				  { "mData": "cnpj" },
				  { "mData": "nome" },
				 // { "defaultContent": '<a href='+ url  + '/editar/' + id +' ><button type="button" class="btn btn-sm btn-success">Editar</button></a>' }
	              { "mData": null,
	                       "mRender": function (oObj) {
	                    	   if (oObj.ativa ==true) {
	                               return '<span class="badge badge-success">Ativa</span>';
	                               }
	                    	   else if (oObj.ativa ==false){
	                    		   return '<span class="badge badge-danger">Inativa</span>';
	                    	   } 
	                    //       return '<span class="badge badge-' + (oObj.ativa == true ? "success" : "danger") + '">' + oObj.ativa + '</span>';
	                      },
	                   "orderable": false},
	                   { "mData": "id" ,                     
	                	   "render": function ( data, type, row, meta) {
	                		   return '<a href="editar/' + row.id+ '"><i class="fas fa-edit"></i></a>';},
	                		   "orderable": false},
			  
			]
	 });
	 setInterval( function () {
		    table.ajax.reload(); // user paging is not reset on reload
		}, 500000 );
	
//	$('#empresaTable tbody').on( 'click', 'button', function () {
//	        var data = table.row( $(this).parents('tr') ).data();
//	        alert( data.codEmp +" é a empresa "+ data.nome );
//	    } );
	
	//atualizar a tabela a cada 30 segundos
	
});