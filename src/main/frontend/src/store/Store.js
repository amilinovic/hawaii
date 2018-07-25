import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import reducer from './reducers';
import randomUserApiSaga from './sagas/randomUserApiSaga';
import getTokenSaga from './sagas/getTokenSaga';
import authenticateSaga from './sagas/authenticateSaga';
import createHistory from 'history/createBrowserHistory';
import { routerMiddleware } from 'react-router-redux';

export const history = createHistory();

const sagaMiddleware = createSagaMiddleware();
const reactRouterMiddleware = routerMiddleware(history);

export default createStore(
  reducer,
  compose(
    applyMiddleware(sagaMiddleware, reactRouterMiddleware),
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  )
);

sagaMiddleware.run(randomUserApiSaga);
sagaMiddleware.run(getTokenSaga);
sagaMiddleware.run(authenticateSaga);
