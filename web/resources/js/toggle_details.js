/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function toggleDisplay(elementID) {

	if (document.getElementById(elementID).style.display === "none") {
		document.getElementById(elementID).style.display = "block";
	} else {
		document.getElementById(elementID).style.display = "none";
	}
	
}

