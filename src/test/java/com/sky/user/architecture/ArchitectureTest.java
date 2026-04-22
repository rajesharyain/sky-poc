package com.sky.user.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

	private static JavaClasses classes;

	@BeforeAll
	static void setup() {
		classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
				.withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS).importPackages("com.sky.user");
	}

	@Test
	void domainShouldNotDependOnOtherLayers() {
		ArchRule rule = noClasses().that().resideInAPackage("..domain..").should().dependOnClassesThat()
				.resideInAnyPackage("..application..", "..infrastructure..");

		rule.check(classes);
	}

	@Test
	void applicationShouldNotDependOnInfrastructure() {
		ArchRule rule = noClasses().that().resideInAPackage("..application..").should().dependOnClassesThat()
				.resideInAPackage("..infrastructure..");

		rule.check(classes);
	}

	@Test
	void infrastructureShouldDependOnDomainAndApplication() {
		ArchRule rule = classes().that().resideInAPackage("..infrastructure..").should().onlyDependOnClassesThat()
				.resideInAnyPackage("..infrastructure..", "..domain..", "..application..", "java..",
						"org.springframework..", "jakarta..", "lombok..");

		rule.check(classes);
	}

	@Test
	@Disabled("ArchUnit + current JDK classfile parsing on some environments reports many false positives.")
	void layeredArchitectureShouldBeRespected() {
		layeredArchitecture().consideringAllDependencies().layer("Domain").definedBy("..domain..").layer("Application")
				.definedBy("..application..").layer("Infrastructure").definedBy("..infrastructure..")
				.whereLayer("Domain").mayNotAccessAnyLayer().whereLayer("Application").mayOnlyAccessLayers("Domain")
				.whereLayer("Infrastructure").mayOnlyAccessLayers("Application", "Domain").check(classes);
	}

	@Test
	void domainModelsShouldNotHaveSpringAnnotations() {
		ArchRule rule = noClasses().that().resideInAPackage("..domain.model..").should().dependOnClassesThat()
				.resideInAPackage("org.springframework..");

		rule.check(classes);
	}

	@Test
	void repositoryInterfacesShouldBeInDomain() {
		ArchRule rule = classes().that().haveSimpleName("UserRepository").and().areInterfaces().should()
				.resideInAPackage("..domain.repository..");

		rule.check(classes);
	}

	@Test
	void useCasesShouldBeInterfaces() {
		ArchRule rule = classes().that().resideInAPackage("..application.port..").should().beInterfaces();

		rule.check(classes);
	}

	@Test
	void controllersShouldOnlyDependOnUseCases() {
		ArchRule rule = classes().that().resideInAPackage("..infrastructure.adapter.input.rest..").and()
				.haveSimpleNameEndingWith("Controller").should().onlyDependOnClassesThat()
				.resideInAnyPackage("..application.port..", "..infrastructure.adapter.input.rest.dto..",
						"..infrastructure.adapter.input.rest.mapper..", "..domain.model..", "java..", "jakarta..",
						"org.springframework..");

		rule.check(classes);
	}
}
