import { handleActions } from 'redux-actions';
import { closeModal, resetModalState } from '../actions/modalActions';

export const initialState = {
  close: false
};

const actionHandlers = {
  [closeModal]: state => {
    return {
      ...state,
      close: true
    };
  },
  [resetModalState]: state => {
    return {
      ...state,
      close: false
    };
  }
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
