import { actions as toastrActions } from 'react-redux-toastr';

export const toastrError = message =>
  toastrActions.add({
    type: 'error',
    title: 'Error',
    message: message
  });

export const toastrSuccess = message =>
  toastrActions.add({
    type: 'success',
    title: 'Success',
    message: message
  });
