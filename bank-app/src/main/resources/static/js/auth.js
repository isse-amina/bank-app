const register = document.querySelectorAll(".create-account");
register.forEach((item, index) => {
    item.addEventListener("click", arrow => {
        location.href = "/register";
    })
})
const cancelRegister = document.querySelector(".cancel-register");
if (cancelRegister != null) {
    cancelRegister.onclick = function() {
        location.href = "/login";
    }
}