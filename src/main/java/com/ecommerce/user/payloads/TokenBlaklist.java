package com.ecommerce.user.payloads;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TokenBlaklist {

	Set<String> tokens = new HashSet<>();
}