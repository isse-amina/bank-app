const register = document.querySelectorAll(".create-account");
register.forEach((item, index) => {
    item.addEventListener("click", arrow => {
        location.href = "/register";
    })
})