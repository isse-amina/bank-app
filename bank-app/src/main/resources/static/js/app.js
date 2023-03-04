const setUp = document.querySelector(".set-up-apple-pay");
if (setUp != null) {
    setUp.onclick = function() {
        location.href = "contact";
    }
}
const dismiss = document.querySelector(".dismiss-apple-pay");
if (dismiss != null) {
    dismiss.onclick = function() {
        document.querySelector(".apple-pay").style.display = "none";
        document.querySelector(".welcome-screen").style.marginTop = "0px";
    }
}
const transferAmount = document.querySelector(".transfer-amount");
if (transferAmount != null) {
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
}
const cancelTransfer = document.querySelector(".cancel-transfer");
if (cancelTransfer != null) {
    cancelTransfer.onclick = function() {
        location.href = "/";
    }
}