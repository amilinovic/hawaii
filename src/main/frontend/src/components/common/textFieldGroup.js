import React from 'react';
import styled from 'styled-components';

const Input = styled.input`
  padding: 7px 0;
  width: 100%;
  font-family: inherit;
  font-size: 14px;
  border-top: 0;
  border-right: 0;
  border-bottom: 1px solid #ddd;
  border-left: 0;
  transition: border-bottom-color 0.25s ease-in;

  &:focus {
    border-bottom-color: #dc3545;
    outline: 0;
  }
`;

const TextFieldGroup = ({ name, placeholder, value, type, onChange }) => {
  return (
    <Input
      type={type}
      placeholder={placeholder}
      name={name}
      value={value}
      onChange={onChange}
    />
  );
};

TextFieldGroup.defaultProps = {
  type: 'text'
};

export default TextFieldGroup;
