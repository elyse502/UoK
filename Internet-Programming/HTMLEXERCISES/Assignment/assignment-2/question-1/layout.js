function setOperator(op) {
    document.getElementById('operator').value = op;
}

function calculate() {
    const num1 = parseFloat(document.getElementById('num1').value);
    const num2 = parseFloat(document.getElementById('num2').value);
    const num3 = parseFloat(document.getElementById('num3').value);
    const operator = document.getElementById('operator').value;
    
    let result;
    switch (operator) {
        case '+':
            result = num1 + num2 + num3;
            break;
        case '-':
            result = num1 - num2 - num3;
            break;
        case '*':
            result = num1 * num2 * num3;
            break;
        case '/':
            result = num1 / num2 / num3;
            break;
        default:
            result = 'Invalid operator';
    }
    
    document.getElementById('answer').value = result;
}

function findEvenOdd() {
    const nums = [
        parseFloat(document.getElementById('num1').value),
        parseFloat(document.getElementById('num2').value),
        parseFloat(document.getElementById('num3').value)
    ];
    
    const evens = nums.filter(num => num % 2 === 0);
    const odds = nums.filter(num => num % 2 !== 0);
    
    document.getElementById('even').value = evens.join(', ');
    document.getElementById('odd').value = odds.join(', ');

    const max = Math.max(...nums);
    const min = Math.min(...nums);

    document.getElementById('max').value = max;
    document.getElementById('min').value = min;
}
