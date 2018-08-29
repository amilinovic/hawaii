import React from 'react';

const TextFieldGroup = ({ name, placeholder, value, type, onChange }) => {
  return (
    <div className="form-group">
      <input
        type={type}
        placeholder={placeholder}
        className="form-control form-control-lg"
        name={name}
        value={value}
        onChange={onChange}
      />
    </div>
  );
};

TextFieldGroup.defaultProps = {
  type: 'text'
};

export default TextFieldGroup;
