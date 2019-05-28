    set linesize 200;
        
    declare
        numOrders number;
        numOrdersInState number;
        numOrdersInFallout number;
        order_proc_time_seconds number;
        order_proc_time varchar2(2000);
        orderids varchar2(9999);
        type orderStates is varray(12) of varchar2(50);
        states orderStates := orderStates(
                'CREATED',
                'ASSESSING_IN_PROGRESS',
                'ASSESSING_ERROR',
                'RUNNING',
                'CANCELLING', 
                'CANCELLED',
                'FAILED',
                'ERROR',
                'DONE', 
                'INVALID', 
                'SUSPENDED', 
                'ABORTED');
        
        cursor falloutCursor
        is
            select distinct order_id
            from om_order_meta
            where state in ('ASSESSING_ERROR','FAILED','ERROR','INVALID');
        falloutOrderIds falloutCursor%ROWTYPE;
        
        procedure GetOrderTotal
        is
        begin
            select count(distinct order_id) into numOrders from om_order;
            dbms_output.put_line('OM Metrics: ' || numOrders || ' orders');
        end;
        
        procedure GetNumOrdersInState(orderState varchar)
        is
        begin
            select count(order_id) into numOrdersInState from om_order_meta where state=orderState;
            DBMS_OUTPUT.PUT_LINE('  ' || numOrdersInState || ' ' || orderState || ' orders');
        end;
        
        procedure GetNumOrdersInFallout
        is
        begin
            select count(distinct order_id) into numOrdersInFallout from om_order_meta where state in ('ASSESSING_ERROR','FAILED','ERROR','INVALID');
            DBMS_OUTPUT.PUT_LINE(numOrdersInFallout || ' orders in fallout [ASSESSING_ERROR,FAILED,ERROR,INVALID] id(s):');
        end;
        
        procedure GetOrderIdFallout
        is
        begin
            for orderIdsInFallout
            in falloutCursor
            loop
                orderids:= orderids || orderIdsInFallout.order_id || ',';
            end loop;
            dbms_output.put_line(orderids);
        end;
        
        procedure GetAvgOrderProcTime
        is
        begin
            with x as 
            (select om_order.order_id,
                    to_timestamp_tz(om_order.created_dtm, 'YY.MM.DD HH24:MI:SS,FF TZR') order_start_date,
                    to_timestamp_tz(om_order_meta.modified_dtm, 'YY.MM.DD HH24:MI:SS,FF TZR') order_end_date,
                    om_order_meta.state
                    from om_order
                    join om_order_meta
                    on
                        om_order.order_id = om_order_meta.order_id
                        and om_order.major_version = 1
                        and om_order.minor_version = 1
                        and om_order_meta.state = 'DONE') 
            select avg(
                    extract(day from (order_end_date - order_start_date))*60*60*24 +
                    extract(hour from (order_end_date - order_start_date))*3600 +
                    extract(minute from (order_end_date - order_start_date))*60 +
                    extract(second from (order_end_date - order_start_date))) into order_proc_time_seconds from x;
                    
            select to_char(trunc(order_proc_time_seconds/86400), 'FM9900') || 'd:' ||
                    to_char(trunc(order_proc_time_seconds/3600),'FM9900') || 'h:' ||
                     to_char(trunc(mod(order_proc_time_seconds,3600)/60),'FM00') || 'm:' ||
                      to_char(mod(order_proc_time_seconds,60),'FM00') || 's' into order_proc_time from dual;
            dbms_output.put_line('average processing time: ' || order_proc_time);
        end;

    begin        
        dbms_output.put_line(to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS'));
        GetOrderTotal;
        dbms_output.put_line(' ');
        for i in 1..12 loop
            GetNumOrdersInState(states(i));
        end loop;
        dbms_output.put_line(' ');
        GetAvgOrderProcTime;
        dbms_output.put_line(' ');
        GetNumOrdersInFallout;
        GetOrderIdFallout;
    end;
    