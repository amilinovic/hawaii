import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import ReduxToastr from 'react-redux-toastr';
import 'reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import store from './store/store';
import './styles/globalStyles.js';

ReactDOM.render(
  <Provider store={store}>
    <div className="d-flex flex-grow-1">
      <ReduxToastr
        timeOut={4000}
        newestOnTop={false}
        preventDuplicates
        position="top-right"
        transitionIn="fadeIn"
        transitionOut="fadeOut"
        progressBar
        duration={4000}
        closeOnToastrClick
      />
      <App />
    </div>
  </Provider>,
  document.getElementById('root')
);
registerServiceWorker();
