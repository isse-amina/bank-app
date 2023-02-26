const transferAmount = document.querySelector(".transfer-amount");
transferAmount.onkeydown = function(e) {
    if (e.keyCode == 189) {
        return false; 
    }    
    var decimals = (this.value + "").split(".")[1];
    if (decimals != undefined && decimals.length == 2) {
        if (e.keyCode != 8 && e.keyCode != 38 && e.keyCode != 40) {
            return false; 
        }
    }
}
transferAmount.onpaste = function() {
    return false; 
}
const cancelTransfer = document.querySelector(".cancel-transfer");
cancelTransfer.onclick = function() {
    location.href = "/";
}