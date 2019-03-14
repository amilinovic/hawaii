import { handleActions } from 'redux-actions';
import { toggleModal } from '../actions/modalActions';

export const initialState = {
  open: false,
  day: undefined
};

const actionHandlers = {
  [toggleModal]: (state, action) => {
    return {
      ...state,
      day: action.payload,
      open: !state.open
    };
  }
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
