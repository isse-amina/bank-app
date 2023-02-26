const setUp = document.querySelector(".set-up-apple-pay");
setUp.onclick = function() {
    location.href = "contact";
}

const dismiss = document.querySelector(".dismiss-apple-pay"); 
dismiss.onclick = function() {
    document.querySelector(".apple-pay").style.display = "none";
    document.querySelector(".welcome-screen").style.marginTop = "0px"; 
}