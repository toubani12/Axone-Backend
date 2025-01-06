import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IAppAccountType } from 'app/entities/app-account-type/app-account-type.model';
import { AppAccountTypeService } from 'app/entities/app-account-type/service/app-account-type.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IAppAccount } from '../app-account.model';
import { AppAccountService } from '../service/app-account.service';
import { AppAccountFormService } from './app-account-form.service';

import { AppAccountUpdateComponent } from './app-account-update.component';

describe('AppAccount Management Update Component', () => {
  let comp: AppAccountUpdateComponent;
  let fixture: ComponentFixture<AppAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appAccountFormService: AppAccountFormService;
  let appAccountService: AppAccountService;
  let userService: UserService;
  let appAccountTypeService: AppAccountTypeService;
  let providerService: ProviderService;
  let employerService: EmployerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppAccountUpdateComponent],
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
      .overrideTemplate(AppAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appAccountFormService = TestBed.inject(AppAccountFormService);
    appAccountService = TestBed.inject(AppAccountService);
    userService = TestBed.inject(UserService);
    appAccountTypeService = TestBed.inject(AppAccountTypeService);
    providerService = TestBed.inject(ProviderService);
    employerService = TestBed.inject(EmployerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const appAccount: IAppAccount = { id: 456 };
      const relatedUser: IUser = { id: 11889 };
      appAccount.relatedUser = relatedUser;

      const userCollection: IUser[] = [{ id: 5746 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [relatedUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AppAccountType query and add missing value', () => {
      const appAccount: IAppAccount = { id: 456 };
      const types: IAppAccountType[] = [{ id: 18464 }];
      appAccount.types = types;

      const appAccountTypeCollection: IAppAccountType[] = [{ id: 9988 }];
      jest.spyOn(appAccountTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: appAccountTypeCollection })));
      const additionalAppAccountTypes = [...types];
      const expectedCollection: IAppAccountType[] = [...additionalAppAccountTypes, ...appAccountTypeCollection];
      jest.spyOn(appAccountTypeService, 'addAppAccountTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      expect(appAccountTypeService.query).toHaveBeenCalled();
      expect(appAccountTypeService.addAppAccountTypeToCollectionIfMissing).toHaveBeenCalledWith(
        appAccountTypeCollection,
        ...additionalAppAccountTypes.map(expect.objectContaining),
      );
      expect(comp.appAccountTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Provider query and add missing value', () => {
      const appAccount: IAppAccount = { id: 456 };
      const providers: IProvider[] = [{ id: 25952 }];
      appAccount.providers = providers;

      const providerCollection: IProvider[] = [{ id: 20950 }];
      jest.spyOn(providerService, 'query').mockReturnValue(of(new HttpResponse({ body: providerCollection })));
      const additionalProviders = [...providers];
      const expectedCollection: IProvider[] = [...additionalProviders, ...providerCollection];
      jest.spyOn(providerService, 'addProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      expect(providerService.query).toHaveBeenCalled();
      expect(providerService.addProviderToCollectionIfMissing).toHaveBeenCalledWith(
        providerCollection,
        ...additionalProviders.map(expect.objectContaining),
      );
      expect(comp.providersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employer query and add missing value', () => {
      const appAccount: IAppAccount = { id: 456 };
      const ifEmployer: IEmployer = { id: 15500 };
      appAccount.ifEmployer = ifEmployer;

      const employerCollection: IEmployer[] = [{ id: 17391 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const additionalEmployers = [ifEmployer];
      const expectedCollection: IEmployer[] = [...additionalEmployers, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(
        employerCollection,
        ...additionalEmployers.map(expect.objectContaining),
      );
      expect(comp.employersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appAccount: IAppAccount = { id: 456 };
      const relatedUser: IUser = { id: 30315 };
      appAccount.relatedUser = relatedUser;
      const type: IAppAccountType = { id: 26934 };
      appAccount.types = [type];
      const provider: IProvider = { id: 10882 };
      appAccount.providers = [provider];
      const ifEmployer: IEmployer = { id: 15612 };
      appAccount.ifEmployer = ifEmployer;

      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(relatedUser);
      expect(comp.appAccountTypesSharedCollection).toContain(type);
      expect(comp.providersSharedCollection).toContain(provider);
      expect(comp.employersSharedCollection).toContain(ifEmployer);
      expect(comp.appAccount).toEqual(appAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppAccount>>();
      const appAccount = { id: 123 };
      jest.spyOn(appAccountFormService, 'getAppAccount').mockReturnValue(appAccount);
      jest.spyOn(appAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appAccount }));
      saveSubject.complete();

      // THEN
      expect(appAccountFormService.getAppAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appAccountService.update).toHaveBeenCalledWith(expect.objectContaining(appAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppAccount>>();
      const appAccount = { id: 123 };
      jest.spyOn(appAccountFormService, 'getAppAccount').mockReturnValue({ id: null });
      jest.spyOn(appAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appAccount }));
      saveSubject.complete();

      // THEN
      expect(appAccountFormService.getAppAccount).toHaveBeenCalled();
      expect(appAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppAccount>>();
      const appAccount = { id: 123 };
      jest.spyOn(appAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAppAccountType', () => {
      it('Should forward to appAccountTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appAccountTypeService, 'compareAppAccountType');
        comp.compareAppAccountType(entity, entity2);
        expect(appAccountTypeService.compareAppAccountType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProvider', () => {
      it('Should forward to providerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(providerService, 'compareProvider');
        comp.compareProvider(entity, entity2);
        expect(providerService.compareProvider).toHaveBeenCalledWith(entity, entity2);
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
  });
});
