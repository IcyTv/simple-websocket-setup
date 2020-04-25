let socket = new WebSocket("ws://localhost:9090");
let msgs = document.getElementById("msg-container");
let inp = document.getElementById("send");
socket.onmessage = (ev) => {
    let text = ev.data;
    console.log(text);
    let p = document.createElement("p");
    p.appendChild(document.createTextNode(text));
    msgs.appendChild(p);
};

inp.onkeydown = (ev) => {
    if(ev.keyCode == 13 && ev.target.value != "") {
        socket.send(ev.target.value);
        ev.target.value = "";
    }
}