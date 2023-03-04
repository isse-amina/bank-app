const adminDelete = document.querySelectorAll(".admin-delete");
const areYouSure = document.querySelectorAll(".are-you-sure");
adminDelete.forEach((item, index) => {
    item.addEventListener("click", arrow => {
        document.querySelector(".overlay").style.display = "block";
        areYouSure[index].style.display = "block";
    })
})
const cancelDeletion = document.querySelectorAll(".cancel-deletion");
cancelDeletion.forEach((item, index) => {
    item.addEventListener("click", arrow => {
        document.querySelector(".overlay").style.display = "none";
        item.parentNode.parentNode.style.display = "none";
    })
})
const cancelAdd = document.querySelectorAll(".cancel-add");
cancelAdd.forEach((item, index) => {
    item.addEventListener("click", arrow => {
        var page = window.location.href;
        if (page.includes("user")) {
            location.href = "/users";
        }
        else if (page.includes("account")) {
            location.href = "/accounts";
        }
        else if (page.includes("transaction")) {
            location.href = "/transactions";
        }
        else {
            location.href = "/admin";
        }
    })
})