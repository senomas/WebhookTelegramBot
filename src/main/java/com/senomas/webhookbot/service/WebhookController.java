package com.senomas.webhookbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@BasePathAwareController
public class WebhookController implements ResourceProcessor<ResourceSupport> {

	@Autowired
	ApplicationContext ctx;

	@Override
	public ResourceSupport process(ResourceSupport resource) {
		resource.add(ControllerLinkBuilder.linkTo(WebhookController.class).withRel("webhook"));
		return resource;
	}

	@RequestMapping(value = "/webhook/{hookType}/{hook}", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<Resource<Long>> post(@PathVariable("hookType") String hookType,
			@PathVariable("hook") String hook, @RequestBody JsonNode payload) throws JsonProcessingException {
		
		WebhookService svc = ctx.getBean(hookType, WebhookService.class);
		
		Resource<Long> resource = new Resource<Long>(svc.process(hook, payload));

		resource.add(ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(WebhookController.class).post("", "", null)).withSelfRel());

		return ResponseEntity.ok(resource);
	}
}
