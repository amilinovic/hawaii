import styled from 'styled-components';

export const StyledLink = styled.span`
  color: ${props => (props.color ? props.color : 'white')};
  padding: 10px;
  border-radius: 5px;
  background: #fb4b4f;
  border: 1px solid #3c3c46;
  display: inline-block;
`;
