let display = document.getElementById('display');
let currentInput = '';
let currentOperator = '';
let firstOperand = '';

function appendNumber(number) {
    currentInput += number;
    display.innerText = currentInput;
}

function setOperator(operator) {
    if (currentInput === '') return;
    if (currentOperator !== '') calculateResult();
    firstOperand = currentInput;
    currentOperator = operator;
    currentInput = '';
}

function clearDisplay() {
    currentInput = '';
    currentOperator = '';
    firstOperand = '';
    display.innerText = '0';
}

function calculateResult() {
    if (currentInput === '' || currentOperator === '' || firstOperand === '') return;
    let result;
    switch (currentOperator) {
        case '+':
            result = parseFloat(firstOperand) + parseFloat(currentInput);
            break;
        case '-':
            result = parseFloat(firstOperand) - parseFloat(currentInput);
            break;
        case '*':
            result = parseFloat(firstOperand) * parseFloat(currentInput);
            break;
        case '/':
            result = parseFloat(firstOperand) / parseFloat(currentInput);
            break;
        default:
            return;
    }
    display.innerText = result;
    currentInput = result.toString();
    currentOperator = '';
    firstOperand = '';
}
