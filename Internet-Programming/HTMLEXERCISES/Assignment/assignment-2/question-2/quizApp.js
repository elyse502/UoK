function calculateResult() {
    let totalMarks = 0;

    const q1 = document.querySelector('input[name="q1"]:checked');
    const q2 = document.querySelector('input[name="q2"]:checked');
    const q3 = document.querySelector('input[name="q3"]:checked');

    if (q1) totalMarks += parseInt(q1.value);
    if (q2) totalMarks += parseInt(q2.value);
    if (q3) totalMarks += parseInt(q3.value);

    document.getElementById('totalMarks').value = totalMarks;
}
