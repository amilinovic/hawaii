import React from 'react';
import ReactSelect from 'react-select';

const defaultStyles = {
  control: state => ({
    ...state,
    width: '300px'
  })
};

const Select = props => {
  return <ReactSelect styles={defaultStyles} options={props.options} />;
};

export default Select;
