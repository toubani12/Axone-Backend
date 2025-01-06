import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { ContractTypeService } from 'app/entities/contract-type/service/contract-type.service';
import { ITemplate } from 'app/entities/template/template.model';
import { TemplateService } from 'app/entities/template/service/template.service';
import { ICriteria } from 'app/entities/criteria/criteria.model';
import { CriteriaService } from 'app/entities/criteria/service/criteria.service';
import { IDomain } from 'app/entities/domain/domain.model';
import { DomainService } from 'app/entities/domain/service/domain.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IApplication } from '../application.model';
import { ApplicationService } from '../service/application.service';
import { ApplicationFormService } from './application-form.service';

import { ApplicationUpdateComponent } from './application-update.component';

describe('Application Management Update Component', () => {
  let comp: ApplicationUpdateComponent;
  let fixture: ComponentFixture<ApplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationFormService: ApplicationFormService;
  let applicationService: ApplicationService;
  let contractTypeService: ContractTypeService;
  let templateService: TemplateService;
  let criteriaService: CriteriaService;
  let domainService: DomainService;
  let employerService: EmployerService;
  let recruiterService: RecruiterService;
  let candidateService: CandidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ApplicationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationFormService = TestBed.inject(ApplicationFormService);
    applicationService = TestBed.inject(ApplicationService);
    contractTypeService = TestBed.inject(ContractTypeService);
    templateService = TestBed.inject(TemplateService);
    criteriaService = TestBed.inject(CriteriaService);
    domainService = TestBed.inject(DomainService);
    employerService = TestBed.inject(EmployerService);
    recruiterService = TestBed.inject(RecruiterService);
    candidateService = TestBed.inject(CandidateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ContractType query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const contractTypes: IContractType[] = [{ id: 12012 }];
      application.contractTypes = contractTypes;

      const contractTypeCollection: IContractType[] = [{ id: 6681 }];
      jest.spyOn(contractTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: contractTypeCollection })));
      const additionalContractTypes = [...contractTypes];
      const expectedCollection: IContractType[] = [...additionalContractTypes, ...contractTypeCollection];
      jest.spyOn(contractTypeService, 'addContractTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(contractTypeService.query).toHaveBeenCalled();
      expect(contractTypeService.addContractTypeToCollectionIfMissing).toHaveBeenCalledWith(
        contractTypeCollection,
        ...additionalContractTypes.map(expect.objectContaining),
      );
      expect(comp.contractTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Template query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const contractTemplates: ITemplate[] = [{ id: 19487 }];
      application.contractTemplates = contractTemplates;

      const templateCollection: ITemplate[] = [{ id: 5355 }];
      jest.spyOn(templateService, 'query').mockReturnValue(of(new HttpResponse({ body: templateCollection })));
      const additionalTemplates = [...contractTemplates];
      const expectedCollection: ITemplate[] = [...additionalTemplates, ...templateCollection];
      jest.spyOn(templateService, 'addTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(templateService.query).toHaveBeenCalled();
      expect(templateService.addTemplateToCollectionIfMissing).toHaveBeenCalledWith(
        templateCollection,
        ...additionalTemplates.map(expect.objectContaining),
      );
      expect(comp.templatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Criteria query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const criteria: ICriteria[] = [{ id: 9152 }];
      application.criteria = criteria;

      const criteriaCollection: ICriteria[] = [{ id: 29061 }];
      jest.spyOn(criteriaService, 'query').mockReturnValue(of(new HttpResponse({ body: criteriaCollection })));
      const additionalCriteria = [...criteria];
      const expectedCollection: ICriteria[] = [...additionalCriteria, ...criteriaCollection];
      jest.spyOn(criteriaService, 'addCriteriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(criteriaService.query).toHaveBeenCalled();
      expect(criteriaService.addCriteriaToCollectionIfMissing).toHaveBeenCalledWith(
        criteriaCollection,
        ...additionalCriteria.map(expect.objectContaining),
      );
      expect(comp.criteriaSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Domain query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const domains: IDomain[] = [{ id: 6401 }];
      application.domains = domains;

      const domainCollection: IDomain[] = [{ id: 2745 }];
      jest.spyOn(domainService, 'query').mockReturnValue(of(new HttpResponse({ body: domainCollection })));
      const additionalDomains = [...domains];
      const expectedCollection: IDomain[] = [...additionalDomains, ...domainCollection];
      jest.spyOn(domainService, 'addDomainToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(domainService.query).toHaveBeenCalled();
      expect(domainService.addDomainToCollectionIfMissing).toHaveBeenCalledWith(
        domainCollection,
        ...additionalDomains.map(expect.objectContaining),
      );
      expect(comp.domainsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employer query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const employer: IEmployer = { id: 12791 };
      application.employer = employer;

      const employerCollection: IEmployer[] = [{ id: 5163 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const additionalEmployers = [employer];
      const expectedCollection: IEmployer[] = [...additionalEmployers, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(
        employerCollection,
        ...additionalEmployers.map(expect.objectContaining),
      );
      expect(comp.employersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Recruiter query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const recruiters: IRecruiter[] = [{ id: 20606 }];
      application.recruiters = recruiters;

      const recruiterCollection: IRecruiter[] = [{ id: 20371 }];
      jest.spyOn(recruiterService, 'query').mockReturnValue(of(new HttpResponse({ body: recruiterCollection })));
      const additionalRecruiters = [...recruiters];
      const expectedCollection: IRecruiter[] = [...additionalRecruiters, ...recruiterCollection];
      jest.spyOn(recruiterService, 'addRecruiterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(recruiterService.query).toHaveBeenCalled();
      expect(recruiterService.addRecruiterToCollectionIfMissing).toHaveBeenCalledWith(
        recruiterCollection,
        ...additionalRecruiters.map(expect.objectContaining),
      );
      expect(comp.recruitersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Candidate query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const candidates: ICandidate[] = [{ id: 21115 }];
      application.candidates = candidates;

      const candidateCollection: ICandidate[] = [{ id: 11965 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const additionalCandidates = [...candidates];
      const expectedCollection: ICandidate[] = [...additionalCandidates, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(
        candidateCollection,
        ...additionalCandidates.map(expect.objectContaining),
      );
      expect(comp.candidatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const application: IApplication = { id: 456 };
      const contractType: IContractType = { id: 14385 };
      application.contractTypes = [contractType];
      const contractTemplate: ITemplate = { id: 12528 };
      application.contractTemplates = [contractTemplate];
      const criteria: ICriteria = { id: 22559 };
      application.criteria = [criteria];
      const domain: IDomain = { id: 26070 };
      application.domains = [domain];
      const employer: IEmployer = { id: 30450 };
      application.employer = employer;
      const recruiters: IRecruiter = { id: 3811 };
      application.recruiters = [recruiters];
      const candidates: ICandidate = { id: 17544 };
      application.candidates = [candidates];

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(comp.contractTypesSharedCollection).toContain(contractType);
      expect(comp.templatesSharedCollection).toContain(contractTemplate);
      expect(comp.criteriaSharedCollection).toContain(criteria);
      expect(comp.domainsSharedCollection).toContain(domain);
      expect(comp.employersSharedCollection).toContain(employer);
      expect(comp.recruitersSharedCollection).toContain(recruiters);
      expect(comp.candidatesSharedCollection).toContain(candidates);
      expect(comp.application).toEqual(application);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplication>>();
      const application = { id: 123 };
      jest.spyOn(applicationFormService, 'getApplication').mockReturnValue(application);
      jest.spyOn(applicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ application });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: application }));
      saveSubject.complete();

      // THEN
      expect(applicationFormService.getApplication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationService.update).toHaveBeenCalledWith(expect.objectContaining(application));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplication>>();
      const application = { id: 123 };
      jest.spyOn(applicationFormService, 'getApplication').mockReturnValue({ id: null });
      jest.spyOn(applicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ application: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: application }));
      saveSubject.complete();

      // THEN
      expect(applicationFormService.getApplication).toHaveBeenCalled();
      expect(applicationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplication>>();
      const application = { id: 123 };
      jest.spyOn(applicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ application });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContractType', () => {
      it('Should forward to contractTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractTypeService, 'compareContractType');
        comp.compareContractType(entity, entity2);
        expect(contractTypeService.compareContractType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTemplate', () => {
      it('Should forward to templateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(templateService, 'compareTemplate');
        comp.compareTemplate(entity, entity2);
        expect(templateService.compareTemplate).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCriteria', () => {
      it('Should forward to criteriaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(criteriaService, 'compareCriteria');
        comp.compareCriteria(entity, entity2);
        expect(criteriaService.compareCriteria).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDomain', () => {
      it('Should forward to domainService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(domainService, 'compareDomain');
        comp.compareDomain(entity, entity2);
        expect(domainService.compareDomain).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployer', () => {
      it('Should forward to employerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employerService, 'compareEmployer');
        comp.compareEmployer(entity, entity2);
        expect(employerService.compareEmployer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRecruiter', () => {
      it('Should forward to recruiterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(recruiterService, 'compareRecruiter');
        comp.compareRecruiter(entity, entity2);
        expect(recruiterService.compareRecruiter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCandidate', () => {
      it('Should forward to candidateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(candidateService, 'compareCandidate');
        comp.compareCandidate(entity, entity2);
        expect(candidateService.compareCandidate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
