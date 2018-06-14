import styled from 'styled-components';
import { prop } from 'styled-tools';

export const StyledLink = styled.span`
  color: ${prop('color', 'white')};
  padding: 10px;
  border-radius: 5px;
  background: #fb4b4f;
  border: 1px solid #3c3c46;
  display: inline-block;
`;
