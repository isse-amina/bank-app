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