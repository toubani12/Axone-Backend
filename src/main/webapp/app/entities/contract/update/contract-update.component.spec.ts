import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITemplate } from 'app/entities/template/template.model';
import { TemplateService } from 'app/entities/template/service/template.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ContractFormService } from './contract-form.service';

import { ContractUpdateComponent } from './contract-update.component';

describe('Contract Management Update Component', () => {
  let comp: ContractUpdateComponent;
  let fixture: ComponentFixture<ContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractFormService: ContractFormService;
  let contractService: ContractService;
  let templateService: TemplateService;
  let candidateService: CandidateService;
  let recruiterService: RecruiterService;
  let employerService: EmployerService;
  let applicationService: ApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContractUpdateComponent],
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
      .overrideTemplate(ContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractFormService = TestBed.inject(ContractFormService);
    contractService = TestBed.inject(ContractService);
    templateService = TestBed.inject(TemplateService);
    candidateService = TestBed.inject(CandidateService);
    recruiterService = TestBed.inject(RecruiterService);
    employerService = TestBed.inject(EmployerService);
    applicationService = TestBed.inject(ApplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call template query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const template: ITemplate = { id: 32746 };
      contract.template = template;

      const templateCollection: ITemplate[] = [{ id: 8064 }];
      jest.spyOn(templateService, 'query').mockReturnValue(of(new HttpResponse({ body: templateCollection })));
      const expectedCollection: ITemplate[] = [template, ...templateCollection];
      jest.spyOn(templateService, 'addTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(templateService.query).toHaveBeenCalled();
      expect(templateService.addTemplateToCollectionIfMissing).toHaveBeenCalledWith(templateCollection, template);
      expect(comp.templatesCollection).toEqual(expectedCollection);
    });

    it('Should call candidate query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const candidate: ICandidate = { id: 3496 };
      contract.candidate = candidate;

      const candidateCollection: ICandidate[] = [{ id: 6819 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const expectedCollection: ICandidate[] = [candidate, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(candidateCollection, candidate);
      expect(comp.candidatesCollection).toEqual(expectedCollection);
    });

    it('Should call Recruiter query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const recruiter: IRecruiter = { id: 25074 };
      contract.recruiter = recruiter;

      const recruiterCollection: IRecruiter[] = [{ id: 11957 }];
      jest.spyOn(recruiterService, 'query').mockReturnValue(of(new HttpResponse({ body: recruiterCollection })));
      const additionalRecruiters = [recruiter];
      const expectedCollection: IRecruiter[] = [...additionalRecruiters, ...recruiterCollection];
      jest.spyOn(recruiterService, 'addRecruiterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(recruiterService.query).toHaveBeenCalled();
      expect(recruiterService.addRecruiterToCollectionIfMissing).toHaveBeenCalledWith(
        recruiterCollection,
        ...additionalRecruiters.map(expect.objectContaining),
      );
      expect(comp.recruitersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employer query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const employer: IEmployer = { id: 10344 };
      contract.employer = employer;

      const employerCollection: IEmployer[] = [{ id: 937 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const additionalEmployers = [employer];
      const expectedCollection: IEmployer[] = [...additionalEmployers, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(
        employerCollection,
        ...additionalEmployers.map(expect.objectContaining),
      );
      expect(comp.employersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const application: IApplication = { id: 30793 };
      contract.application = application;

      const applicationCollection: IApplication[] = [{ id: 16999 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [application];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contract: IContract = { id: 456 };
      const template: ITemplate = { id: 12690 };
      contract.template = template;
      const candidate: ICandidate = { id: 15257 };
      contract.candidate = candidate;
      const recruiter: IRecruiter = { id: 19320 };
      contract.recruiter = recruiter;
      const employer: IEmployer = { id: 24679 };
      contract.employer = employer;
      const application: IApplication = { id: 8507 };
      contract.application = application;

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(comp.templatesCollection).toContain(template);
      expect(comp.candidatesCollection).toContain(candidate);
      expect(comp.recruitersSharedCollection).toContain(recruiter);
      expect(comp.employersSharedCollection).toContain(employer);
      expect(comp.applicationsSharedCollection).toContain(application);
      expect(comp.contract).toEqual(contract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue(contract);
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractService.update).toHaveBeenCalledWith(expect.objectContaining(contract));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue({ id: null });
      jest.spyOn(contractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(contractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTemplate', () => {
      it('Should forward to templateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(templateService, 'compareTemplate');
        comp.compareTemplate(entity, entity2);
        expect(templateService.compareTemplate).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareRecruiter', () => {
      it('Should forward to recruiterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(recruiterService, 'compareRecruiter');
        comp.compareRecruiter(entity, entity2);
        expect(recruiterService.compareRecruiter).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareApplication', () => {
      it('Should forward to applicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationService, 'compareApplication');
        comp.compareApplication(entity, entity2);
        expect(applicationService.compareApplication).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
