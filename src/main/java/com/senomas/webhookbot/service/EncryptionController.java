package com.senomas.webhookbot.service;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.senomas.boot.security.SenomasEncryptor;

@BasePathAwareController
public class EncryptionController implements ResourceProcessor<ResourceSupport> {
	
	@Override
	public ResourceSupport process(ResourceSupport resource) {
		resource.add(ControllerLinkBuilder.linkTo(EncryptionController.class).withRel("encrypt"));
		return resource;
	}

	@RequestMapping(value = "/encrypt", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Resource<String>> encrypt(@RequestParam(name = "text") String text) {
		String result = SenomasEncryptor.encrypt(text);

		Resource<String> resource = new Resource<String>(result);

		resource.add(ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(EncryptionController.class).encrypt("")).withSelfRel());

		return ResponseEntity.ok(resource);
	}
}
