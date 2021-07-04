function toogleDatatableSummary(element) {
    $span = $(element).find(".ui-button-icon-left"); 
    if($span.hasClass("ui-icon-plus")){
        $span.removeClass("ui-icon-plus");
        $span.addClass("ui-icon-minusthick");
    }else{
        $span.removeClass("ui-icon-minusthick");
        $span.addClass("ui-icon-plus");
    } 
    $(element).closest("tr").next("tr").toggle("fast");
}


JsonToTable = {};

// Builds the HTML Table out of jsonList json data
JsonToTable.build = function (tableId, data) {
	 var $table = $("#"+tableId);
	 var htmlString = JsonToTable.getHtml(data);
	 $table.append(htmlString);
}	


JsonToTable.getHtml = function (data) {
   
    var keys = Object.keys(Object.assign({}, ...data));// Get the keys to make the header
    // Add header
    var head = '<thead><tr>';
    keys.forEach(function(key) {
      head += '<th>'+key+'</th>';
    });
    head += '</tr></thead>';
    // Add body
    var body = '<tbody>';
    data.forEach(function(obj) { // For each row
      var row = '<tr>';
      keys.forEach(function(key) { // For each column
        row += '<td>';
        var object = obj[key];
        if(Array.isArray(object) && object.length > 0 && typeof object[0] === 'object' ){
             row += '<table>'+JsonToTable.getHtml(object)+'</table>';		
        }else{
            if (obj.hasOwnProperty(key)) { // If the obj doesnt has a certain key, add a blank space.
              row += object;
            }
        }
        row += '</td>';
      });
      body += row+'<tr>';
    })
    return head+body+'</tbody>';
 };
 
