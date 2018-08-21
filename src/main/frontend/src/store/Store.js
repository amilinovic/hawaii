import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import reducer from './reducers';
import saga from './sagas';
import { createBrowserHistory } from 'history';
import { connectRouter, routerMiddleware } from 'connected-react-router';

export const history = createBrowserHistory();

const sagaMiddleware = createSagaMiddleware();
const reactRouterMiddleware = routerMiddleware(history);

export default createStore(
  connectRouter(history)(reducer),
  compose(
    applyMiddleware(sagaMiddleware, reactRouterMiddleware),
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  )
);

sagaMiddleware.run(saga);
