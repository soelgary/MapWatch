$.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        //return true;
        console.log('yepp');
        console.log(data);
        var checked = data[10] == '1';
        var filter = $('#filterSelect').val();
        if(filter == 'all') {
          console.log('true');
          return true;
        } else if(filter == 'read') {
          if(checked) {
            console.log('true');
            return true;
          }  
        } else {
          if(!checked) {
            console.log('true');
            return true;
          }
        }
        console.log('false');
        return false;
    }
);


$(document).ready(function() {
  
    var bingTable = $('#bingUpdatesTable').dataTable({
        "ajax": "/update/all",
        "columns": [
            { "data": "mapProvider" },
            { "data": "country" },
            { "data": "id" },
            { "data": "old.path" },
            { "data": "new.path" },
            { "data": "old.hash" },
            { "data": "new.hash" },
            { "data": "old.fetchJob" },
            { "data": "new.fetchJob" },
            { "data": "date" },
            {"data": "read"},
            {"data": "read"}
        ],
        "columnDefs": [
            {
              "targets": [10],
              "visible": false,
            },
            {
                render: function(data, type, row) {
                  return new Date(data).toDateString();
                },
                "targets": 9
            },
            {
              render: function(data, type, row) {
                var id = "bingUpdate" + row['id'];
                var checked = "";
                if(data == 1) {
                  checked = "checked";
                }
                return "<input class='checkbox' type='checkbox' id='" + id + "'" + checked + ">";
              },
              "targets": 11
            }
        ]
    }).columnFilter(
      {
      aoColumns: [ { type: "select", values: [ 'Bing', 'Google']  },
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null
        ]

    }
    );

  $( "#filterSelect" ).change(function() {
    bingTable.fnDraw();
  });

  $('#bingUpdatesTable').on( 'init.dt', function () {
    console.log('init');
    $('input[type="checkbox"]').on("click", function(event) {
      console.log(this.id);
        $.ajax({
          type: "PUT",
          url: "/update/bing/" + this.id.substring(10) + "/read/" + this.checked,
          dataType: "script"
        });
    });
  });
  

  

  

});
