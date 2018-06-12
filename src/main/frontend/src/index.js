import React from 'react';
import ReactDOM from 'react-dom';
import 'reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';
import baseStyles from './styles/BaseStyles';

baseStyles();

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
