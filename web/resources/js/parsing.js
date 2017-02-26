/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function correctCroChars() {
    
    //var table = document.getElementsByClassName("order-table-left-centered-column");
    //for (var i = 0; i < table.length; i++) {ui-panel
    //    table[i].innerHTML = table[i].innerHTML.replace(/plinjac/g,"plinjač");
    //    table[i].innerHTML = table[i].innerHTML.replace(/okretac/g,"okretač");
    //    table[i].innerHTML = table[i].innerHTML.replace(/cica/g,"ćica");
    //}
    
    var table = document.getElementsByClassName("order-table-left-centered-column");
    for (var i = 0; i < table.length; i++) {
        table[i].innerHTML = table[i].innerHTML.replace(/plinjac/g,"plinjač");
        table[i].innerHTML = table[i].innerHTML.replace(/okretac/g,"okretač");
        table[i].innerHTML = table[i].innerHTML.replace(/cica/g,"ćica");
        table[i].innerHTML = table[i].innerHTML.replace(/cepljen/g,"čepljen");
        table[i].innerHTML = table[i].innerHTML.replace(/okrece/g,"okreće");
    }
    
    
}

function correctCroChars1() {
    
    var table = document.getElementsByClassName("order-table-left-centered-column");
    for (var i = 0; i < table.length; i++) {
        table[i].innerHTML = table[i].innerHTML.replace(/ÄŤ/g,"č");
        table[i].innerHTML = table[i].innerHTML.replace(/Ä‡/g,"ć");
    }
    
    var table = document.getElementsByClassName("ui-panel");
    for (var i = 0; i < table.length; i++) {
        //window.alert(table[i].innerHTML);
        table[i].innerHTML = table[i].innerHTML.replace(/ÄŤ/g,"č");
        table[i].innerHTML = table[i].innerHTML.replace(/Ä‡/g,"ć");
    }
 
    
}

function parseNodeBreaklines() {


    var node_list = document.getElementsByClassName("ui-diagram-element");
    var start_node = document.getElementsByClassName("ui-diagram-start");
    var end_node = document.getElementsByClassName("ui-diagram-end");
    
    
    var table = document.getElementsByClassName("ui-diagram-draggable");
    for (var i = 0; i < node_list.length; i++) {
        table[i].innerHTML = table[i].innerHTML.replace(/plinjac/g,"plinjač");
        table[i].innerHTML = table[i].innerHTML.replace(/okretac/g,"okretač");
        table[i].innerHTML = table[i].innerHTML.replace(/cica/g,"ćica");
        table[i].innerHTML = table[i].innerHTML.replace(/cepljen/g,"čepljen");
        table[i].innerHTML = table[i].innerHTML.replace(/okrece/g,"okreće");
    }
    
    
    
    //
    //
    //
    //
//window.alert(node_list.length);
    //window.alert(start_node.length);
    //window.alert(end_node.length);
       
    //document.body = document.body.replace(/ÄŤ/g,"č");
    //document.body.innerHTML = document.body.innerHTML.replace("Ä‡","ć");
    
    //for (var i = 0; i < node_list.length; i++) {
    //  //  node_list[i].innerHTML = node_list[i].innerHTML.replace("&lt;br&gt;","<br>");
    //    node_list[i].innerHTML = node_list[i].innerHTML.replace(/&lt;br&gt;/g,"<br>");
    //    node_list[i].innerHTML = node_list[i].innerHTML.replace(/&#xa;/g,"<br>");
    //    node_list[i].innerHTML = node_list[i].innerHTML.replace("ÄŤ","č");
    //    node_list[i].innerHTML = node_list[i].innerHTML.replace("Ä‡","ć");
       
        
        //var count = (node_list[i].match(/<br>/g) || []).length;
    //}
    
    //for (var i = 0; i < start_node.length; i++) {
    //    start_node[i].innerHTML = start_node[i].innerHTML.replace("&lt;br/&gt;","<br>");
    //    start_node[i].innerHTML = start_node[i].innerHTML.replace(/&lt;br&gt;/g,"<br>");
    //    start_node[i].innerHTML = start_node[i].innerHTML.replace(/&#xa;/g,"<br>");
    //    start_node[i].innerHTML = start_node[i].innerHTML.replace("ÄŤ","č");
    //    start_node[i].innerHTML = start_node[i].innerHTML.replace("Ä‡","ć");
    //}
    
    //for (var i = 0; i < end_node.length; i++) {
        //window.alert(end_node[i].innerHTML);
    //    end_node[i].innerHTML = end_node[i].innerHTML.replace("&lt;br/&gt;","<br>");
    //    end_node[i].innerHTML = end_node[i].innerHTML.replace(/&lt;br&gt;/g,"<br>");
    //    end_node[i].innerHTML = end_node[i].innerHTML.replace(/&#xa;/g,"<br>");
    //    end_node[i].innerHTML = end_node[i].innerHTML.replace("ÄŤ","č");
    //    end_node[i].innerHTML = end_node[i].innerHTML.replace("Ä‡","ć");
    //}
    
    correctCroChars();
}


