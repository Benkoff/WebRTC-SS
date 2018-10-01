$(function(){
    const uuidInput = document.querySelector('input#uuid');

    function guid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }

    if (localStorage.getItem("uuid") === null) {
        localStorage.setItem("uuid", guid());
    }
    uuidInput.value = localStorage.getItem("uuid");
    console.log("local.uuid:" + localStorage.getItem("uuid"));
    // console.log("input.value:" + uuidInput.value);
});

function addUuidToButtonLink(button) {
    let id = 'button-link-' + button.value;
    let ref = document.getElementById(id).href;
    document.getElementById(id).href = ref + '/user/' + localStorage.getItem("uuid");
    console.log("link.href:" + document.getElementById(id).href);
}
