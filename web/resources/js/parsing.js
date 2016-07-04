/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function parseNodeBreaklines() {

    var node_list = document.getElementsByClassName("ui-diagram-element");
    var start_node = document.getElementsByClassName("ui-diagram-start");
    var end_node = document.getElementsByClassName("ui-diagram-end");

    for (var i = 0; i < node_list.length; i++) {
        node_list[i].innerHTML = node_list[i].innerHTML.replace(/&lt;br&gt;/g,"<br>");
        node_list[i].innerHTML = node_list[i].innerHTML.replace(/&#xa;/g,"<br>");
        
        //var count = (node_list[i].match(/<br>/g) || []).length;
    }
    
    for (var i = 0; i < start_node.length; i++) {
        start_node[i].innerHTML = start_node[i].innerHTML.replace(/&lt;br&gt;/g,"<br>");
        start_node[i].innerHTML = start_node[i].innerHTML.replace(/&#xa;/g,"<br>");
    }
    
    for (var i = 0; i < end_node.length; i++) {
        end_node[i].innerHTML = end_node[i].innerHTML.replace(/&lt;br&gt;/g,"<br>");
        end_node[i].innerHTML = end_node[i].innerHTML.replace(/&#xa;/g,"<br>");
    }
    
    
}


