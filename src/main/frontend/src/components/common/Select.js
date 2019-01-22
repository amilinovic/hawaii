import React from 'react';
import ReactSelect from 'react-select';

const defaultStyles = {
  control: state => ({
    ...state,
    width: '300px',
    margin: 30
  })
};

const Select = props => {
  return (
    <ReactSelect
      styles={defaultStyles}
      options={props.options}
      onChange={props.change}
    />
  );
};

export default Select;
