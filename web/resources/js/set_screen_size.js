/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function set_screen_size(form_id) {
    document.getElementById(form_id+":height").value = $(document).height();
    document.getElementById(form_id+":width").value = $(document).width();
    //alert(document.getElementById(form_id+":height").value);
    //alert(document.getElementById(form_id).value);
    

}


