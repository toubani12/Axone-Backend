import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { ContractTypeService } from '../service/contract-type.service';
import { IContractType } from '../contract-type.model';
import { ContractTypeFormService } from './contract-type-form.service';

import { ContractTypeUpdateComponent } from './contract-type-update.component';

describe('ContractType Management Update Component', () => {
  let comp: ContractTypeUpdateComponent;
  let fixture: ComponentFixture<ContractTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractTypeFormService: ContractTypeFormService;
  let contractTypeService: ContractTypeService;
  let applicationService: ApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContractTypeUpdateComponent],
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
      .overrideTemplate(ContractTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractTypeFormService = TestBed.inject(ContractTypeFormService);
    contractTypeService = TestBed.inject(ContractTypeService);
    applicationService = TestBed.inject(ApplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Application query and add missing value', () => {
      const contractType: IContractType = { id: 456 };
      const applications: IApplication[] = [{ id: 3415 }];
      contractType.applications = applications;

      const applicationCollection: IApplication[] = [{ id: 14726 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [...applications];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contractType: IContractType = { id: 456 };
      const application: IApplication = { id: 9860 };
      contractType.applications = [application];

      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      expect(comp.applicationsSharedCollection).toContain(application);
      expect(comp.contractType).toEqual(contractType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractType>>();
      const contractType = { id: 123 };
      jest.spyOn(contractTypeFormService, 'getContractType').mockReturnValue(contractType);
      jest.spyOn(contractTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractType }));
      saveSubject.complete();

      // THEN
      expect(contractTypeFormService.getContractType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractTypeService.update).toHaveBeenCalledWith(expect.objectContaining(contractType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractType>>();
      const contractType = { id: 123 };
      jest.spyOn(contractTypeFormService, 'getContractType').mockReturnValue({ id: null });
      jest.spyOn(contractTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractType }));
      saveSubject.complete();

      // THEN
      expect(contractTypeFormService.getContractType).toHaveBeenCalled();
      expect(contractTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractType>>();
      const contractType = { id: 123 };
      jest.spyOn(contractTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
