$(function() {

	$("#tabelaOrdenar").tablesorter({
		sortList: [[0,0],[1,0],[2,0]],
		theme: 'bootstrap',
		  headerTemplate: '{content} {icon}',
		  widgets : [ 'zebra', 'columns', 'uitheme' ],
	});
})