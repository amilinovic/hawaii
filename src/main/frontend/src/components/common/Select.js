import React from 'react';
import ReactSelect from 'react-select';

const defaultStyles = {
  control: (base, state) => ({
    ...base,
    width: '300px',
    border: '1px solid #b3b3b3',
    marginTop: 20,
    borderRadius: '4px',
    outline: 'none',
    boxShadow: 'none',
    ':hover': {
      border: '1px solid #b3b3b3'
    }
  })
};

const Select = props => {
  return (
    <ReactSelect
      styles={defaultStyles}
      options={props.options}
      onChange={props.change}
      theme={theme => ({
        ...theme,
        borderRadius: 0,
        colors: {
          ...theme.colors,
          primary25: '#b3b3b3',
          primary: '#2c3238',
          primary50: '#b3b3b3'
        }
      })}
    />
  );
};

export default Select;
