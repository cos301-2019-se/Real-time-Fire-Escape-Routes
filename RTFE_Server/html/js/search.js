function search(searchBox,tableElement) {
  console.log(searchBox,tableElement);
  var input, filter, table, tr, td, i, txtValue;
  input = searchBox
  filter = input.value.toUpperCase();
  table = $("#"+tableElement);
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    var show= false;
    for (var col = 0; col < tr.getElementsByTagName("td").length; col++) {
      var column = tar[i].getElementsByTagName("td")[col];
      if(column){
        txtValue = column.textContent || column.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
        } else {
          show = true;
        }
      }
    }
    if (show) {
      tr[i].style.display = "";
    } else {
      tr[i].style.display = "none";
    }
  }
}