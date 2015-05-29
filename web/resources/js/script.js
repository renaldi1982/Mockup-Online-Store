$(document).ready(function(){
    var checkbox = $("#signupform\\:changepassword");
    var inputPassword = $("#signupform\\:password");
    var password = $("#signupform\\:password").val();
    
    if(checkbox.is(':checked')){
        checkbox.attr('checked',false);
    }
    
    checkbox.on('change',function(){
        if(checkbox.is(':checked')){
            inputPassword.val("");           
        }else{
            inputPassword.val(password);          
        }
    });                    
    
    $("#showcaseproductform\\:productstableitem th").each(function(i){
        var remove = 0;
        
        /* Find all columns of this row */
        var tds = $(this).parents("table").find('tr td:nth-child(' + (i + 1) + ')');
        
        /* Iterate through each column, check if cell contains an empty string 
         * and increment remove if it is */
        tds.each(function(j) {
           if(this.innerHTML === '') remove++; 
        });
        
        /* Check if remove is equal to */
        if(remove === ($("#showcaseproductform\\:productstableitem tr").length - 1)){
            $(this).hide();
            tds.hide();
        }
    });
});

