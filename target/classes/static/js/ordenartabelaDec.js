$(function() {

	$("#tabelaOrdenar").tablesorter({
		sortList: [[3,0],[2,0],[0,0]],
		theme: 'bootstrap',
		  headerTemplate: '{content} {icon}',
		  widgets : [ 'zebra', 'columns', 'uitheme' ],
		/*  widthFixed : true,
		  widgetOptions : {
	          filter_columnFilters : true,
	          filter_ignoreCase : true,
	          filter_liveSearch : true,
	          filter_resetOnEsc : true,
	          filter_searchDelay : 300,
		  		}*/
	});
})