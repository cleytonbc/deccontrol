$(function() {

	$("#tabelaOrdenar").tablesorter({
		sortList: [[0,0],[1,0]],
		theme: 'bootstrap',
		  headerTemplate: '{content} {icon}',
		  widgets : [ 'zebra', 'columns', 'filter', 'uitheme' ]
	});
})